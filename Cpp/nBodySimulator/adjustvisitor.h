#ifndef COMPONENTVISITOR_H
#define COMPONENTVISITOR_H
#include "visitor.h"
#include "form.h"

class AdjustVisitor : public Visitor
{
public:
    AdjustVisitor();
    ~AdjustVisitor();

    const Form& getForm() const { return form; }
    void visit (UniverseBody* body);

private:
    Form form;
};

#endif // COMPONENTVISITOR_H
