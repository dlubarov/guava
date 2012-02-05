package d;

import static d.Opcodes.*;

import java.util.Map;

import common.*;
import d.nat.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;

public class BytecodeMethodDef extends ConcreteMethodDef {
    public final String[] stringTable;

    private final int numLocals;
    private final int[] code;

    public BytecodeMethodDef(RawMethod desc,
            RawType[] typeDescTable,
            TypeDesc[] fullTypeDescTable,
            RawMethod[] methodDescTable,
            String[] stringTable,
            Map<RawMethod, RawMethod> vDescTable,
            int numLocals, int[] bytecode) {
        super(desc, typeDescTable, fullTypeDescTable, methodDescTable, vDescTable);

        for (int i = 0; i < stringTable.length; ++i)
            stringTable[i] = stringTable[i].intern();
        this.stringTable = stringTable;

        this.numLocals = numLocals;
        this.code = bytecode;
    }

    @Override
    public void invoke(BaseObject[] stack, int bp, ConcreteType[] genericArgs) {
        int sp = bp + numLocals, ip = 0, op;

        for (;;) {
            try {
                op = code[ip++];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NiftyException("execution ran off the end of %s without returning", desc);
            }

            switch (op) {
                case POP:
                    --sp;
                    break;

                case DUP: {
                    BaseObject a = stack[sp];
                    stack[++sp] = a;
                    break;
                }

                case CONST_INT:
                    stack[++sp] = new NativeInt(code[ip++]);
                    break;

                case CONST_CHAR:
                    stack[++sp] = new NativeChar((char) code[ip++]);
                    break;

                case CONST_TRUE:
                    stack[++sp] = NativeBool.TRUE;
                    break;

                case CONST_FALSE:
                    stack[++sp] = NativeBool.FALSE;
                    break;

                case CONST_STRING:
                    stack[++sp] = VMUtils.makeString(stringTable[ip++]);
                    break;

                case GET_LOCAL: {
                    int i = code[ip++]; // local index
                    stack[++sp] = stack[bp + i + 1];
                    break;
                }

                case PUT_LOCAL: {
                    int i = code[ip++]; // local index
                    stack[bp + i + 1] = stack[sp--];
                    break;
                }

                case GET_STATIC_FIELD: {
                    int i = code[ip++]; // index into type table
                    int j = code[ip++]; // static field index
                    stack[++sp] = rawTypeTable[i].staticFields[j];
                    break;
                }

                case PUT_STATIC_FIELD: {
                    BaseObject a = stack[sp--];
                    int i = code[ip++]; // index into type table
                    int j = code[ip++]; // static field index
                    rawTypeTable[i].staticFields[j] = a;
                    break;
                }

                case GET_INSTANCE_FIELD: {
                    int i = code[ip++]; // index into string table
                    String fieldName = stringTable[i];
                    assert fieldName.intern() == fieldName : "String in literal pool wasn't interned.";

                    BaseObject a = stack[sp--]; // target
                    i = a.type.rawType.virtualFieldTable.get(fieldName); // field index
                    stack[++sp] = a.fields[i];
                    break;
                }

                case PUT_INSTANCE_FIELD: {
                    int i = code[ip++]; // index into string table
                    String fieldName = stringTable[i];
                    assert fieldName.intern() == fieldName : "String in literal pool wasn't interned.";

                    BaseObject b = stack[sp--]; // new field value
                    BaseObject a = stack[sp--]; // target
                    i = a.type.rawType.virtualFieldTable.get(fieldName); // field index
                    a.fields[i] = b;
                    break;
                }

                case INVOKE_STATIC: {
                    int i = code[ip++]; // index into method table
                    ConcreteMethodDef m = (ConcreteMethodDef) methodTable[i];
                    BaseObject a = desc.isStatic ? null : stack[bp + 1]; // current object

                    // Create array of generic arguments.
                    i = code[ip++]; // # generic args
                    ConcreteType[] newGenericArgs = new ConcreteType[i];
                    for (int j = 0; j < i; ++j)
                        newGenericArgs[j] = fullTypeTable[code[ip++]].toConcrete(a, genericArgs);

                    // Invoke the method.
                    i = m.desc.paramTypes.length;
                    if (!m.desc.isStatic)
                        ++i;
                    m.invoke(stack, sp - i, newGenericArgs);
                    sp -= i - 1;
                    break;
                }

                case INVOKE_VIRTUAL: {
                    int i = code[ip++]; // index into method table
                    MethodDef m = methodTable[i];
                    assert !m.desc.isStatic : "virtual execution of static method";
                    BaseObject a = desc.isStatic ? null : stack[bp + 1]; // current object

                    // Create array of generic arguments.
                    i = code[ip++]; // # generic args
                    ConcreteType[] newGenericArgs = new ConcreteType[i];
                    for (int j = 0; j < i; ++j)
                        newGenericArgs[j] = fullTypeTable[code[ip++]].toConcrete(a, genericArgs);

                    i = m.desc.paramTypes.length; // # args
                    a = stack[sp - i]; // target
                    TypeDef targetOwner = a.type.rawType;
                    targetOwner.virtualMethodTable.get(m).invoke(stack, sp - i - 1, newGenericArgs);
                    break;
                }

                case NEW: {
                    int i = code[ip++]; // index into full type table
                    BaseObject a = desc.isStatic ? null : stack[bp + 1]; // current object
                    stack[++sp] = fullTypeTable[i].toConcrete(a, genericArgs).rawInstance();
                    break;
                }

                case JUMP: {
                    int i = code[ip++];
                    ip += i;
                    break;
                }

                case JUMP_COND: {
                    int i = code[ip++];
                    if (((NativeBool) stack[sp--]).value)
                        ip += i;
                    break;
                }

                case BOOL_NEG:
                    stack[sp] = new NativeBool(!((NativeBool) stack[sp]).value);
                    break;

                case RETURN:
                    // TODO: this copy shouldn't be necessary if the INVOKE_s accounted for
                    // numLocals of the invoked method when reading the return value
                    stack[bp + 1] = stack[sp];
                    return;

                default:
                    throw new NiftyException("bad opcode in %s: %d", desc, op);
            }
        }
    }
}
