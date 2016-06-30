#ifndef CENSUSFORM_H
#define CENSUSFORM_H
#include <limits>
/**
 * @brief The CensusForm class
 * This is a collection of data that is passed around inside the composite
 * to collect the real time position information.
 */
class Form {
public:
    Form ()
        : xsum(0)
        , ysum(0)
        , population(0)
        , min_x(std::numeric_limits<double>::max())
        , min_y(std::numeric_limits<double>::max())
        , max_x(-std::numeric_limits<double>::max())
        , max_y(-std::numeric_limits<double>::max())
    {}

public:
    double xsum;
    double ysum;
    int population;
    double min_x;
    double min_y;
    double max_x;
    double max_y;
};

#endif // CENSUSFORM_H
