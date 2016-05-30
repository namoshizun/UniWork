#ifndef VISITOR_H
#define VISITOR_H
#include "form.h"
class UniverseBody;
class UniverseComposite;
class UniverseComponent;

class Visitor
{
public:
    Visitor();
    virtual ~Visitor();

    void visit (UniverseComposite* composite);
    void visit (UniverseComponent* component);
    virtual void visit (UniverseBody* body) = 0;
};

#endif // VISITOR_H
