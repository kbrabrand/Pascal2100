package no.uio.ifi.pascal2100.parser;

public abstract class PascalDecl extends PascalSyntax {
    String name, progProcFuncName;
    int declLevel = 0, declOffset = 0;
    //Del 3: Type type = null;

    PascalDecl(String id, int lNum) {
	super(lNum);
	name = id;
    }


    /**
     * checkWhetherAssignable: Utility method to check whether this PascalDecl is
     * assignable, i.e., may be used to the left of a :=. 
     * The compiler must check that a name is used properly;
     * for instance, using a variable name a in "a()" is illegal.
     * This is handled in the following way:
     * <ul>
     * <li> When a name a is found in a setting which implies that should be
     *      assignable, the parser will first search for a's declaration d.
     * <li> The parser will call d.checkWhetherAssignable(this).
     * <li> Every sub-class of PascalDecl will implement a checkWhetherAssignable.
     *      If the declaration is indeed assignable, checkWhetherAssignable will do
     *      nothing, but if it is not, the method will give an error message.
     * </ul>
     * Examples
     * <dl>
     *  <dt>VarDecl.checkWhetherAssignable(...)</dt>
     *  <dd>will do nothing, as everything is all right.</dd>
     *  <dt>TypeDecl.checkWhetherAssignable(...)</dt>
     *  <dd>will give an error message.</dd>
     * </dl>
     */
    /*Del 3:
    abstract void checkWhetherAssignable(PascalSyntax where);

    abstract void checkWhetherFunction(PascalSyntax where);
    abstract void checkWhetherProcedure(PascalSyntax where);
    abstract void checkWhetherValue(PascalSyntax where);
    */
}
