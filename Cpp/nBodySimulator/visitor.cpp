#include "visitor.h"
#include "universecomponent.h"
#include "universecomposite.h"
#include "universebody.h"
Visitor::Visitor() {}
Visitor::~Visitor() {}

void Visitor::visit(UniverseComponent *component)
{
    switch (component->getType()) {
    case planet :
    case star :
    case blackhole :
        visit (dynamic_cast<UniverseBody*>(component));
        break;

    case solarsystem:
    case galaxy:
    case cluster:
    case universe:
        visit (dynamic_cast<UniverseComposite*>(component));
        break;
    }
}

void Visitor::visit(UniverseComposite *composite)
{
    for (UniverseComponent* child : composite->getChildren())
        visit(child);
}
