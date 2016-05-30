#ifndef UNIVERSE_FACTORY_H
#define UNIVERSE_FACTORY_H

#include "universebody.h"
#include "universecomponent.h"
#include "universecomposite.h"
#include <QColor>
#include <string>
#include <unordered_map>


class UniverseComponentFactory
{
public:
    UniverseComponentFactory();
    virtual ~UniverseComponentFactory();

    //This is what's known as a "Parameterised factory method"
    //It decides which class to create based on the parameter passed in
    //It also handles the construction of the complex object, like a Builder
    virtual UniverseComponent* createUniverseComponent(
            const std::unordered_map<std::string, std::string>& block) const;

private:
    //Create a leaf type (planet, star, blackhole)
    virtual UniverseComponent* createLeaf(
            const std::unordered_map<std::string, std::string>& block,
            const std::string& name,
            const std::string& parentName,
            UniverseComponentType type) const;

    //Create a composite type (cluster, galaxy, solarsystem)
    virtual UniverseComponent* createComposite(
            const std::unordered_map<std::string, std::string>& block,
            const std::string& name,
            const std::string& parentName,
            UniverseComponentType type) const;

    //convert a #123456 colour to a QColor
    QColor parseColor(const std::string& value) const;

    //get a double from a block
    double getDouble(
            const std::unordered_map<std::string, std::string>& block,
            const std::string& key,
            const std::string& name,
            bool required = true) const;

    //convert a string to a UniverseComponentType
    UniverseComponentType stringToUniverseComponentType(const std::string& type) const;

};

#endif // FACTORY_H
