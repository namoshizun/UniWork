#ifndef CENSUSVISITOR_H
#define CENSUSVISITOR_H
#include "visitor.h"
#include "config.h"

class InspectVisitor : public Visitor
{
public:
    InspectVisitor(int x, int y);
    ~InspectVisitor();

    void visit (UniverseBody* body);
    UniverseBody* getTarget() { return target; }

private:
    UniverseBody* target;
    int xpos;
    int ypos;
};

#endif // CENSUSVISITOR_H
