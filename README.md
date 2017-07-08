[![Build Status](https://travis-ci.org/konsolas/ConditionalCommands.svg?branch=master)](https://travis-ci.org/konsolas/ConditionalCommands)
# ConditionalCommands
Only execute a command if a condition is met.

ConditionalCommands is intended to be used when plugins have automatic commands that should only be executed if certain conditions are met. It is able to execute multiple commands, with customisable delays on each command.

## Usage
```
/cc <player> unless <condition> do <command>
/cc <player> if <condition> do <command>
/cc help
```

### Conditions
Grammar of conditions:
```
<expression>::=<term>{<or><term>}
<term>::=<factor>{<and><factor>}
<factor>::=<comparison>|<not><factor>|(<expression>)
<comparison>::=<constant><comparator><constant>
<constant>::=floating point number or integer
<and>::='&'
<or>::='|'
<not>::='!'
<comparator>::='>'|'='|'<'
```
As shown above, only numbers can be compared, and placeholders can only consist of numbers. In case of multiple comparison operators in a group, i.e. 3>=<2, only the first operator will be used. Comparisons cannot include spaces. Inequality may be checked with !(value=value).

Examples:
```
/cc konsolas unless -ping->200 do kick konsolas
/cc konsolas if (-ping-<300&-ping->100)&-tps->15.0 do msg konsolas Your ping is between 300 and 100, and the TPS is greater than 15.
```

### Placeholders
Placeholders are delimited by '-'. Since they're applied with a replace, errors will probably be detected during parsing if they are typed incorrectly.

 - ```ping``` - The latency of the tested player.
 - ```tps``` - Server TPS average over the last 2 seconds
 - ```time_online``` - Player's online time in milliseconds
 - ```uptime``` - Server uptime in ticks
 - ```player_count``` - Number of players on the server
 - ```perm:<permission>``` - 1.0 if the player has the permission, 0.0 otherwise. e.g. ```-perm:essentials.home-```
 - ```aacvl:<check>``` - AAC violation level of the given check (internal name). e.g. ```-aacvl:speed-```

### Multi command / delayed commands
In the 'do' clause of the statement, multiple commands can be executed at once, and selected commands can be delayed if desired. The command delimiter is ```/<delay>/```, where the integer between ```/``` and ```/``` denotes the delay before the command should be executed in ticks. Here are some examples:


```
/cc konsolas if -aacvl:heuristics->0 do /1200/ ban konsolas 1 minute delayed ban for killaura
/cc konsolas if -perm:some.permission-=1 do /0/ broadcast konsolas has some.permission! /0/ broadcast second broadcast! /20/ broadcast 1 second later!
```
