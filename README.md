# ODataParser
scala parser to convert an OData filter string into a collection of operation objects.

this class is a refactor of some custom parsing code that was originally written in java. When doing the same logic in java it had to broken up into two classes. One to handle the parsing of parens. The other to then parse out the logic and simple operations.

Once we have the operation tree then it is possible to use this to generate proper selectors for the data repository being used. In the case of the code that this is used in we are using a postgresql database and using QueryDsl to generate the sql query from the list of operations.
