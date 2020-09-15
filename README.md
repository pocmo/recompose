# recompose

recompose is a tool for converting [Android layouts in XML](https://developer.android.com/guide/topics/ui/declaring-layout)
to Kotlin code using [Jetpack Compose](https://developer.android.com/jetpack/compose). It can be used on the command line,
as IntelliJ / Android Studio plugin or as a library in custom code.

## Building

Either import the project into [IntelliJ IDEA](https://www.jetbrains.com/idea/) or use [Gradle](https://gradle.org/) on
the command line (via the provided `gradlew` wrapper).

### Modules

* **recompose-ast**: Contains the data classes for the Abstract Syntax Tree (AST) representing a parsed XML layout.
* **recompose-cli**: A command-line interface (CLI) for running recompose in a shell.
* **recompose-composer**: Responsible for taking an AST and transforming it into the equivalent `Composable` Kotlin code.
* **recompose-idea**: An IntelliJ IDEA / Android Studio plugin that allows copying XML layouts and pasting as
`Composable` Kotlin code.
* **recompose-runtime**: A (not so serious) "composer" implementation that turns XML layouts into the equivalent
`Composable` code at runtime. Think of an interpreter that interprets XML layouts at runtime, executing matching
`Composable`s.
* **recompose-test**: Contains test data and helpers for unit tests. 

### Important gradle tasks

* **clean**: Deletes the build directories.
* **test**: Runs all unit tests in all modules.
* **runIde**: Runs IntelliJ IDEA with the plugin installed. 
* **buildPlugin**: Builds the IntelliJ / Android Studio plugin and makes it available in `recompose-idea/build/distributions`.

## License

```
Copyright 2020 Sebastian Kaspari

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```