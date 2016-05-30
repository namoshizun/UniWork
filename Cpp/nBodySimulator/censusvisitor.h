#ifndef CENSUSVISITOR_H
#define CENSUSVISITOR_H
#include "visitor.h"
#include "config.h"

class CensusVisitor : public Visitor
{
public:
    CensusVisitor(int x, int y);
    ~CensusVisitor();

    void visit (UniverseBody* body);
    UniverseBody* getTarget() { return target; }

private:
    UniverseBody* target;
    int xpos;
    int ypos;
};

#endif // CENSUSVISITOR_H
