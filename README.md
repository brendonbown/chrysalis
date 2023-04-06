# chrysalis
A command line program that helps to manage project permissions in BYU OIT

## Terminal commands
* `add`
  * adds a list of permissions to the given user
  * syntax: `chrys add PERM_ONE PERM_TWO PERM_THREE...`
* `remove`
  * removes a list of permissions from the given user
  * syntax: `chrys remove PERM_ONE PERM_TWO PERM_THREE...`
* `list`
  * lists the permissions currently assigned to the given user
  * syntax: `chrys list`
* `version`
  * prints the version number

Also, you can always do `chrys --help`, that'll give you all the different options.
(Ignore the 'prod-perm' action and the '-P'/'--product' option, though,
the transition to Tyk broke that one)

