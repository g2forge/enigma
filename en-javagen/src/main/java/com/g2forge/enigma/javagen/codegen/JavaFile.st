JavaFile(this, separator) ::= <<
package <this.packageName>;

<if(this.imports)><this.imports:{import | <JavaImport(import, "\n")>}>

<endif>
<(this.declaration.class.simpleName)(this.declaration, "")>

>>
