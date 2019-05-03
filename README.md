<div align="center">
  <a href="https://rkonovalov.github.io/projects/comparator/1.0.0/">
    <img src="https://rkonovalov.github.io/assets/images/comparator-logo.svg" alt="Comparator Main page">
  </a>
  <br>
</div>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/rkonovalov/comparator.svg?branch=master)](https://travis-ci.org/rkonovalov/comparator)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.rkonovalov/comparator/badge.svg?style=blue)](https://search.maven.org/search?q=a:comparator)
[![Javadocs](https://www.javadoc.io/badge/com.github.rkonovalov/comparator.svg)](https://www.javadoc.io/doc/com.github.rkonovalov/comparator)
[![codecov](https://codecov.io/gh/rkonovalov/comparator/branch/master/graph/badge.svg)](https://codecov.io/gh/rkonovalov/comparator)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ea8708461ffb49108013aa0f5ec09ede)](https://www.codacy.com/app/rkonovalov/comparator?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=rkonovalov/comparator&amp;utm_campaign=Badge_Grade)
[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=comparator&metric=alert_status)](https://sonarcloud.io/dashboard?id=comparator)

# About Comparator
Most of all languages has conditional statements. And Java is not exception.
Condition statements help you to test one or more values to correctness.

Look at the next example:
```java
    if(condition) {
      //block of code to be executed if the condition is true
    }
```

If condition is true then we will execute code in braces. And if condition is false we can execute another code, like in example:
```java
    if(condition) {
      //block of code to be executed if the condition is true
    } else {
      //block of another code to be executed if the condition is false
    }
```

If we need to compare multiple conditions we can use **else if** statement:
```java
    if(strValue.equals("result_1")) {
      //block of code to be executed if strValue equals result_1
    } else if(strValue.equals("result_2")) {
           //block of code to be executed if strValue equals result_2
    } else if(strValue.equals("result_3")) {
           //block of code to be executed if strValue equals result_3
    }
    ...
```
As you can see with increasing of complexity of comparing the code becomes less readable.

Of course you can use **switch** statement like in next example:
```java
    switch(intValue) {
        case 1: 
            //block of code to be executed if intValue equals 1
            break;
        case 2: 
            //block of code to be executed if intValue equals 2
            break;
        case 3: 
            //block of code to be executed if intValue equals 3
            break;
    }
    ...
```
But **switch** statement has restrictions. It accepts only types: char, byte, short, int, Character, Byte, Short, Integer, String or an enum
If we try to test some complex types we forced to use "if...else" statements

Comparator can to solve this type of issues. Let's look closer how it works.

## Installation
For using Comparator you need to import dependency

```xml
<dependency>
    <groupId>com.github.rkonovalov</groupId>
    <artifactId>comparator</artifactId>
    <version>1.0.0</version>
</dependency>
```
If you are using another build automation tool, you can find configuration string by this URL:
https://search.maven.org/artifact/com.github.rkonovalov/comparator/1.0.0/jar

## Examples o usage
Comparator uses Java 8 lambda expressions and Optional container. And if you ever had experience with using it, it will be useful.

## Simple comparing
If you need just test object for equality you can use next code
```java
    String strValue = "test";
    String result = Comparator.of(strValue)
        .compare("test", "Found result 1")
        .compare("test2", "Found result 2")
        .get();
    
    //result value will be equal to "Found result 1" string
```

## Else equivalent
In case of Comparator didn't find equality of object value, it can return default value like in next example:

```java
    String strValue = null;
    String result = Comparator.of(strValue)
        .compare("test", "Found result 1")
        .compare("test2", "Found result 2")
        .orElse("Not found");
    
    //result value will be equal to "Not found" string
```

## Complex comparing
How about testing more than one object value? 
Comparator allows you using Java 8 predicates and resultExpression functions.

```java
   String strValue = "test";
   String result = Comparator.of(strValue)
       .compare((s -> s.length() == 4), (s -> "Length of string 4 char"))
       .compare((s -> s.startsWith("st")), (s -> "Found st prefix in string"))
       .get();
   
   //result value will be equal to "Length of string 4 char" string
```
## Comparing with strong typing
If you need to control return values in mapping functions you can use next example
```java
   String strValue = "test";
   String result = Comparator.of(strValue, String.class)
       .compare((s -> s.length() == 4), (s -> "Length of string 4 char"))
       .compare((s -> s.startsWith("st")), (s -> "Found st prefix in string"))
       .get();
   
   //result value will be equal to "Length of string 4 char" string
```
Compiler will check all return values in match method. All return values should be instantiated from String class
Next code will not be compiled by compiler
```java
   String strValue = "test";
   String result = Comparator.of(strValue, String.class)
       .compare((s -> s.length() == 4), (s -> "Length of string 4 char"))
       .compare((s -> s.startsWith("st")), (s -> "Found st prefix in string".length()))
       .get();
   
   //result value will be equal to "Length of string 4 char" string
```
Because next code will return value type of int, not of String
```java
    ...
    .compare((s -> s.startsWith("st")), (s -> "Found st prefix in string".length()))
    ...
```
## Release notes

### Version 1.0.0
    * Initial release
