package org.stringtemplate.v4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.stringtemplate.v4.compiler.Bytecode;
import org.stringtemplate.v4.compiler.Compiler;
import org.stringtemplate.v4.debug.EvalTemplateEvent;
import org.stringtemplate.v4.misc.ErrorManager;
import org.stringtemplate.v4.misc.ErrorType;
import org.stringtemplate.v4.misc.Misc;
import org.stringtemplate.v4.misc.STNoSuchAttributeException;

public class AdvancedInterpreter extends Interpreter {
	public AdvancedInterpreter(STGroup group, boolean debug) {
		super(group, debug);
	}

	public AdvancedInterpreter(STGroup group, ErrorManager errMgr, boolean debug) {
		super(group, errMgr, debug);
	}

	public AdvancedInterpreter(STGroup group, Locale locale, boolean debug) {
		super(group, locale, debug);
	}

	public AdvancedInterpreter(STGroup group, Locale locale, ErrorManager errMgr, boolean debug) {
		super(group, locale, errMgr, debug);
	}

	protected Object process(Object operand) {
		return STAttributeGenerator.unwrap(operand);
	}
	
	protected int _exec(STWriter out, ST self) {
		int start = out.index(); // track char we're about to write
		int prevOpcode = 0;
		int n = 0; // how many char we write out
		int nargs;
		int nameIndex;
		int addr;
		String name;
		Object o, left, right;
		ST st;
		Object[] options;
		byte[] code = self.impl.instrs;        // which code block are we executing
		int ip = 0;
		while ( ip < self.impl.codeSize ) {
			if ( trace || debug ) trace(self, ip);
			short opcode = code[ip];
			//count[opcode]++;
			current_ip = ip;
			ip++; //jump to next instruction or first byte of operand
			switch (opcode) {
				case Bytecode.INSTR_LOAD_STR :
					// just testing...
					load_str(self,ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					break;
				case Bytecode.INSTR_LOAD_ATTR :
					nameIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					name = self.impl.strings[nameIndex];
					try {
						o = getAttribute(self, name);
						if ( o==ST.EMPTY_ATTR ) o = null;
					}
					catch (STNoSuchAttributeException nsae) {
						errMgr.runTimeError(this, self, current_ip, ErrorType.NO_SUCH_ATTRIBUTE, name);
						o = null;
					}
					operands[++sp] = process(o);
					break;
				case Bytecode.INSTR_LOAD_LOCAL:
					int valueIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					o = self.locals[valueIndex];
					if ( o==ST.EMPTY_ATTR ) o = null;
					operands[++sp] = process(o);
					break;
				case Bytecode.INSTR_LOAD_PROP :
					nameIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					o = operands[sp--];
					name = self.impl.strings[nameIndex];
					operands[++sp] = process(getObjectProperty(out, self, o, name));
					break;
				case Bytecode.INSTR_LOAD_PROP_IND :
					Object propName = operands[sp--];
					o = operands[sp];
					operands[sp] = process(getObjectProperty(out, self, o, propName));
					break;
				case Bytecode.INSTR_NEW :
					nameIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					name = self.impl.strings[nameIndex];
					nargs = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					// look up in original hierarchy not enclosing template (variable group)
					// see TestSubtemplates.testEvalSTFromAnotherGroup()
					st = self.groupThatCreatedThisInstance.getEmbeddedInstanceOf(this, self, ip, name);
					// get n args and store into st's attr list
					storeArgs(self, nargs, st);
					sp -= nargs;
					operands[++sp] = st;
					break;
				case Bytecode.INSTR_NEW_IND:
					nargs = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					name = (String)operands[sp-nargs];
					st = self.groupThatCreatedThisInstance.getEmbeddedInstanceOf(this, self, ip, name);
					storeArgs(self, nargs, st);
					sp -= nargs;
					sp--; // pop template name
					operands[++sp] = st;
					break;
				case Bytecode.INSTR_NEW_BOX_ARGS : {
					nameIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					name = self.impl.strings[nameIndex];
					@SuppressWarnings("unchecked")
					Map<String,Object> attrs = (Map<String,Object>)operands[sp--];
					// look up in original hierarchy not enclosing template (variable group)
					// see TestSubtemplates.testEvalSTFromAnotherGroup()
					st = self.groupThatCreatedThisInstance.getEmbeddedInstanceOf(this, self, ip, name);
					// get n args and store into st's attr list
					storeArgs(self, attrs, st);
					operands[++sp] = st;
					break;
				}
				case Bytecode.INSTR_SUPER_NEW :
					nameIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					name = self.impl.strings[nameIndex];
					nargs = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					super_new(self, name, nargs);
					break;
				case Bytecode.INSTR_SUPER_NEW_BOX_ARGS : {
					nameIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					name = self.impl.strings[nameIndex];
					@SuppressWarnings("unchecked")
					Map<String,Object> attrs = (Map<String,Object>)operands[sp--];
					super_new(self, name, attrs);
					break;
				}
				case Bytecode.INSTR_STORE_OPTION:
					int optionIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					o = operands[sp--];    // value to store
					options = (Object[])operands[sp]; // get options
					options[optionIndex] = o; // store value into options on stack
					break;
				case Bytecode.INSTR_STORE_ARG: {
					nameIndex = getShort(code, ip);
					name = self.impl.strings[nameIndex];
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					o = operands[sp--];
					@SuppressWarnings("unchecked")
					Map<String,Object> attrs = (Map<String,Object>)operands[sp];
					attrs.put(name, o); // leave attrs on stack
					break;
				}
				case Bytecode.INSTR_WRITE :
					o = operands[sp--];
					int n1 = writeObjectNoOptions(out, self, o);
					n += n1;
					nwline += n1;
					break;
				case Bytecode.INSTR_WRITE_OPT :
					options = (Object[])operands[sp--]; // get options
					o = operands[sp--];                 // get option to write
					int n2 = writeObjectWithOptions(out, self, o, options);
					n += n2;
					nwline += n2;
					break;
				case Bytecode.INSTR_MAP :
					st = (ST)operands[sp--]; // get prototype off stack
					o = operands[sp--];		 // get object to map prototype across
					map(self,o,st);
					break;
				case Bytecode.INSTR_ROT_MAP :
					int nmaps = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					List<ST> templates = new ArrayList<ST>();
					for (int i=nmaps-1; i>=0; i--) templates.add((ST)operands[sp-i]);
					sp -= nmaps;
					o = operands[sp--];
					if ( o!=null ) rot_map(self,o,templates);
					break;
				case Bytecode.INSTR_ZIP_MAP:
					st = (ST)operands[sp--];
					nmaps = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					List<Object> exprs = new ArrayList<Object>();
					for (int i=nmaps-1; i>=0; i--) exprs.add(operands[sp-i]);
					sp -= nmaps;
					operands[++sp] = zip_map(self, exprs, st);
					break;
				case Bytecode.INSTR_BR :
					ip = getShort(code, ip);
					break;
				case Bytecode.INSTR_BRF :
					addr = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					o = operands[sp--]; // <if(expr)>...<endif>
					if ( !testAttributeTrue(o) ) ip = addr; // jump
					break;
				case Bytecode.INSTR_OPTIONS :
					operands[++sp] = new Object[Compiler.NUM_OPTIONS];
					break;
				case Bytecode.INSTR_ARGS:
					operands[++sp] = new HashMap<String,Object>();
					break;
				case Bytecode.INSTR_PASSTHRU : {
					nameIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					name = self.impl.strings[nameIndex];
					@SuppressWarnings("unchecked")
					Map<String,Object> attrs = (Map<String,Object>)operands[sp];
					passthru(self, name, attrs);
					break;
				}
				case Bytecode.INSTR_LIST :
					operands[++sp] = new ArrayList<Object>();
					break;
				case Bytecode.INSTR_ADD : {
					o = operands[sp--];             // pop value
					@SuppressWarnings("unchecked")
					List<Object> list = (List<Object>)operands[sp]; // don't pop list
					addToList(list, o);
					break;
				}
				case Bytecode.INSTR_TOSTR :
					// replace with string value; early eval
					operands[sp] = toString(out, self, operands[sp]);
					break;
				case Bytecode.INSTR_FIRST  :
					operands[sp] = process(first(operands[sp]));
					break;
				case Bytecode.INSTR_LAST   :
					operands[sp] = process(last(operands[sp]));
					break;
				case Bytecode.INSTR_REST   :
					operands[sp] = process(rest(operands[sp]));
					break;
				case Bytecode.INSTR_TRUNC  :
					operands[sp] = process(trunc(operands[sp]));
					break;
				case Bytecode.INSTR_STRIP  :
					operands[sp] = process(strip(operands[sp]));
					break;
				case Bytecode.INSTR_TRIM   :
					o = operands[sp--];
					if ( o.getClass() == String.class ) {
						operands[++sp] = ((String)o).trim();
					}
					else {
						errMgr.runTimeError(this, self, current_ip, ErrorType.EXPECTING_STRING, "trim", o.getClass().getName());
						operands[++sp] = process(o);
					}
					break;
				case Bytecode.INSTR_LENGTH :
					operands[sp] = length(operands[sp]);
					break;
				case Bytecode.INSTR_STRLEN :
					o = operands[sp--];
					if ( o.getClass() == String.class ) {
						operands[++sp] = ((String)o).length();
					}
					else {
						errMgr.runTimeError(this, self, current_ip, ErrorType.EXPECTING_STRING, "strlen", o.getClass().getName());
						operands[++sp] = 0;
					}
					break;
				case Bytecode.INSTR_REVERSE :
					operands[sp] = process(reverse(operands[sp]));
					break;
				case Bytecode.INSTR_NOT :
					operands[sp] = !testAttributeTrue(operands[sp]);
					break;
				case Bytecode.INSTR_OR :
					right = operands[sp--];
					left = operands[sp--];
					operands[++sp] = testAttributeTrue(left) || testAttributeTrue(right);
					break;
				case Bytecode.INSTR_AND :
					right = operands[sp--];
					left = operands[sp--];
					operands[++sp] = testAttributeTrue(left) && testAttributeTrue(right);
					break;
				case Bytecode.INSTR_INDENT :
					int strIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					indent(out, self, strIndex);
					break;
				case Bytecode.INSTR_DEDENT :
					out.popIndentation();
					break;
				case Bytecode.INSTR_NEWLINE :
					try {
						if ( prevOpcode==Bytecode.INSTR_NEWLINE ||
							prevOpcode==Bytecode.INSTR_INDENT ||
							nwline>0 )
						{
							out.write(Misc.newline);
						}
						nwline = 0;
					}
					catch (IOException ioe) {
						errMgr.IOError(self, ErrorType.WRITE_IO_ERROR, ioe);
					}
					break;
				case Bytecode.INSTR_NOOP :
					break;
				case Bytecode.INSTR_POP :
					sp--; // throw away top of stack
					break;
				case Bytecode.INSTR_NULL :
					operands[++sp] = null;
					break;
				case Bytecode.INSTR_TRUE :
					operands[++sp] = true;
					break;
				case Bytecode.INSTR_FALSE :
					operands[++sp] = false;
					break;
				case Bytecode.INSTR_WRITE_STR :
					strIndex = getShort(code, ip);
					ip += Bytecode.OPND_SIZE_IN_BYTES;
					o = self.impl.strings[strIndex];
					n1 = writeObjectNoOptions(out, self, o);
					n += n1;
					nwline += n1;
					break;
				// TODO: generate this optimization
//				case Bytecode.INSTR_WRITE_LOCAL:
//					valueIndex = getShort(code, ip);
//					ip += Bytecode.OPND_SIZE_IN_BYTES;
//					o = self.locals[valueIndex];
//					if ( o==ST.EMPTY_ATTR ) o = null;
//					n1 = writeObjectNoOptions(out, self, o);
//					n += n1;
//					nwline += n1;
//					break;
				default :
					errMgr.internalError(self, "invalid bytecode @ "+(ip-1)+": "+opcode, null);
					self.impl.dump();
			}
			prevOpcode = opcode;
		}
		if ( debug ) {
			int stop = out.index() - 1;
			EvalTemplateEvent e = new EvalTemplateEvent(currentScope, start, stop);
			trackDebugEvent(self, e);
		}
		return n;
	}
}
