package d;

import static d.Opcodes.*;

import common.*;
import d.nat.*;
import d.ty.ConcreteType;
import d.ty.desc.TypeDesc;

public class BytecodeMethodDef extends ConcreteMethodDef {
    public final String[] stringTable;

    private final int numLocals; // includes "this"
    private final int[] code;

    public BytecodeMethodDef(RawMethod desc,
            RawType[] typeDescTable,
            TypeDesc[] fullTypeDescTable,
            RawMethod[] methodDescTable,
            String[] stringTable,
            int numLocals, int[] bytecode) {
        super(desc, typeDescTable, fullTypeDescTable, methodDescTable);

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
                    BaseObject object = stack[sp];
                    stack[++sp] = object;
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

                case CREATE_SEQ: {
                    int len = code[ip++];
                    BaseObject[] contents = new BaseObject[len];
                    for (int i = 1; i <= len; ++i)
                        contents[len - i] = stack[sp--];
                    stack[++sp] = new NativeMutableArray(contents);
                    break;
                }

                case GET_LOCAL: {
                    int localIndex = code[ip++];
                    stack[++sp] = stack[bp + localIndex + 1];
                    break;
                }

                case PUT_LOCAL: {
                    int localIndex = code[ip++];
                    stack[bp + localIndex + 1] = stack[sp--];
                    break;
                }

                case GET_STATIC_FIELD: {
                    int typeTableIndex = code[ip++];
                    int staticFieldIndex = code[ip++];
                    stack[++sp] = rawTypeTable[typeTableIndex].staticFields[staticFieldIndex];
                    break;
                }

                case PUT_STATIC_FIELD: {
                    BaseObject newValue = stack[sp--];
                    int typeTableIndex = code[ip++];
                    int staticFieldIndex = code[ip++];
                    rawTypeTable[typeTableIndex].staticFields[staticFieldIndex] = newValue;
                    break;
                }

                case GET_INSTANCE_FIELD: {
                    int stringTableIndex = code[ip++];
                    String fieldName = stringTable[stringTableIndex];
                    assert fieldName.intern() == fieldName : "String in literal pool wasn't interned.";

                    BaseObject target = stack[sp--];
                    int fieldIndex = target.type.rawType.virtualFieldTable.get(fieldName);
                    stack[++sp] = target.fields[fieldIndex];
                    break;
                }

                case PUT_INSTANCE_FIELD: {
                    int stringTableIndex = code[ip++];
                    String fieldName = stringTable[stringTableIndex];
                    assert fieldName.intern() == fieldName : "String in literal pool wasn't interned.";

                    BaseObject newValue = stack[sp--];
                    BaseObject target = stack[sp--];
                    int fieldIndex = target.type.rawType.virtualFieldTable.get(fieldName);
                    target.fields[fieldIndex] = newValue;
                    break;
                }

                case INVOKE_STATIC: {
                    int methodTableIndex = code[ip++];
                    ConcreteMethodDef m = (ConcreteMethodDef) methodTable[methodTableIndex];
                    BaseObject a = desc.isStatic ? null : stack[bp + 1]; // current object

                    // Create array of generic arguments.
                    int numGenericArgs = m.desc.numGenericParams;
                    ConcreteType[] newGenericArgs;
                    if (numGenericArgs == 0)
                        // We don't want to allocate an empty array, that's wasteful.
                        newGenericArgs = null;
                    else {
                        newGenericArgs = new ConcreteType[numGenericArgs];
                        for (int j = 0; j < numGenericArgs; ++j)
                            newGenericArgs[j] = fullTypeTable[code[ip++]].toConcrete(a, genericArgs);
                    }

                    // Invoke the method.
                    int numArgs = m.desc.paramTypes.length;
                    if (!m.desc.isStatic)
                        ++numArgs;
                    m.invoke(stack, sp - numArgs, newGenericArgs);
                    sp -= numArgs - 1;
                    break;
                }

                case INVOKE_VIRTUAL: {
                    int methodTableIndex = code[ip++]; // index into method table
                    MethodDef m = methodTable[methodTableIndex];
                    assert !m.desc.isStatic : "virtual execution of static method";
                    BaseObject a = desc.isStatic ? null : stack[bp + 1]; // current object

                    // Create array of generic arguments.
                    int numGenericArgs = m.desc.numGenericParams;
                    ConcreteType[] newGenericArgs;
                    if (numGenericArgs == 0)
                        // We don't want to allocate an empty array, that's wasteful.
                        newGenericArgs = null;
                    else {
                        newGenericArgs = new ConcreteType[numGenericArgs];
                        for (int j = 0; j < numGenericArgs; ++j)
                            newGenericArgs[j] = fullTypeTable[code[ip++]].toConcrete(a, genericArgs);
                    }

                    int numArgs = m.desc.paramTypes.length;
                    a = stack[sp - numArgs]; // target
                    TypeDef targetOwner = a.type.rawType;
                    targetOwner.virtualMethodTable.get(m).invoke(stack, sp - numArgs - 1, newGenericArgs);
                    break;
                }

                case NEW: {
                    int typeTableIndex = code[ip++];
                    BaseObject me = desc.isStatic ? null : stack[bp + 1]; // "this"
                    stack[++sp] = fullTypeTable[typeTableIndex].toConcrete(me, genericArgs).rawInstance();
                    break;
                }

                case JUMP: {
                    int offset = code[ip++];
                    ip += offset;
                    break;
                }

                case JUMP_COND: {
                    int offset = code[ip++];
                    if (((NativeBool) stack[sp--]).value)
                        ip += offset;
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
