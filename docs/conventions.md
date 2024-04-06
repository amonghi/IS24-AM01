# Conventions

This page details a few simple conventions that we as a team choose to follow to keep our code and repo somewhat tidy.

It must be noted that these conventions were not adopted at the absolute beginning of the project, so they have not been
followed from the start.

## Code conventions

### `TODO`s and `FIXME`s

To mark an area of code that will need to be touched up later, use:

* `// TODO:`
    * if there is some feature that has not been implemented yet
    * if something will need to be decided later (maybe after consulting with other teammates)
* `// FIXME:` if there is something that doesn't quite work yet and needs fixing later

### Test classes

The tests relating to the class `Game` should be put in a class named `GameTest`.

## Commits

### Commit messages

We are going to loosely follow the [Conventional Commits specification](https://www.conventionalcommits.org/en/v1.0.0/).

All commits must be structured as follows:

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

`<type>` must be one of:

* `feat:` A new feature
* `fix:` A bug fix
* `docs:` Documentation only changes
* `test:` Adding missing tests or correcting existing tests
* `refactor:` A code change that neither fixes a bug nor adds a feature
* `chore:` A code change that relates to:
    * adding/removing/updating dependencies
    * configuration changes to the build system
    * code formatting

`scope` can be whatever string surrounded by parenthesis.
We don't have a list of defined scopes: just write whatever thing may help to understand the commit better, or just
write nothing.

#### Examples

`chore: formatted README`

`docs(Player class): add missing @throws in javadoc`

```
docs(UML): align with code

Added classes Bingo and Bongo.
Updated doTheThing() method arguments.

Co-authored-by: Matteo Garzone <matteo.garzone@mail.polimi.it>
```

### Pre-commit checklist

Before commiting to other branches, do the following:

* [ ] format code
* [ ] check for typos
* [ ] read **all** the warnings
    * for each warning:
        * solve it right now if you can
        * if you cant solve it right now, add a [TODO](#todos-and-fixmes)
        * if the warning can be safely ignored, suppress it with a comment
* [ ] build project & run tests
    * do not commit code that does not compile
    * you can commit code that does not pass the tests, but avoid if possible
* [ ] check what branch you are on
    * do not commit to `main`

## Branches

### `main` branch

All code commited into `main` must compile and all tests must pass.

**Never** commit directly to `main`. Commit to other branches and then open a pull request to merge into `main`.

#### Pre-merge checklist

Before accepting a pull request into `main`, do the following:

* [ ] the [Pre-commit checklist](#pre-commit-checklist)
* [ ] build project & run tests
    * do not merge code that does not compile
    * do not merge code that does not pass the tests

### Other branches

Other branches are owned by whomever created them.
No one is allowed to commit to a branch which they do not own without previously coordinating with the branch owner.

### Branch naming

Branch names must be names as follows:

```
<type>/<descriptive name>
```

`<type>` can be one of the types from the [Commit messages](#commit-messages) (must use the same meaning) or something
else as long as it is helpful to understand the purpose of this branch.

`<descriptive name>` can be any string. The purpose is to make it clear what the purpose of this branch is.
