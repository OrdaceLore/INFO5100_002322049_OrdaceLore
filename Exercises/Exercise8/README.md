# Exercise 8: XML and JSON Parsing

This exercise demonstrates XML and JSON parsing and manipulation in Java.

## Overview
The BookParser program:
- Reads and parses data from XML and JSON files
- Displays the content in a formatted way
- Programmatically adds new book entries to both formats
- Displays the updated structures

## Files
- `BookParser.java`: Main program file
- `books.xml`: Sample XML data file
- `books.json`: Sample JSON data file
- `lib/json-20210307.jar`: JSON library for Java

## How to Run

```
java -cp .:lib/json-20210307.jar BookParser.java
```

> Note: On Windows, use semicolons instead of colons in the classpath:
> ```
> javac -cp .;lib/json-20210307.jar BookParser.java
> java -cp .;lib/json-20210307.jar BookParser
> ```

## Expected Output
The program will display:
1. The original XML content from books.xml
2. The XML content after adding a new book
3. The original JSON content from books.json
4. The JSON content after adding a new book

## Dependencies
- Java Development Kit (JDK)
- org.json library (included in the lib directory) 