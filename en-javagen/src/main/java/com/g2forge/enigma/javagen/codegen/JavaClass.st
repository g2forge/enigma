JavaClass(this, separator) ::= <<
<AJavaAnnotated(this, "")><JavaProtection(this.protection, " ")>class <this.name> {<if(this.members)>
	<this.members:{member | <(member.class.simpleName)(member, "")>};separator="\n\n">
<endif>}<separator>
>>
