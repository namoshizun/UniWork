#include "censusvisitor.h"
#include "universebody.h"

CensusVisitor::CensusVisitor(int x, int y) :
    Visitor()
  , xpos(x)
  , ypos(y)
  , target(NULL)
{
}

CensusVisitor::~CensusVisitor()
{
}

void CensusVisitor::visit(UniverseBody *body)
{
    Config* config = Config::getInstance();
    if (std::abs(body->getPositionX() / config->getDistanceScale()
                 + body->getPositionY() / config->getDistanceScale()
                 - xpos - ypos) < 6)
        target = body;
}
