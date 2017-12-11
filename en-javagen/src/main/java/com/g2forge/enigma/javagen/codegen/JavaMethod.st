JavaMethod(this, separator) ::= <<
<AJavaAnnotated(this, "")><JavaProtection(this.protection, " ")><JavaType(this.type, " ")><this.name>(<this.arguments:{argument | <JavaVariable(argument, "")>};separator=", ">) {<if(this.statements)>
	<this.statements:{statement | <(statement.class.simpleName)(statement, ";")>};separator="\n">
<endif>}<separator>
>>
