# ConditionalCommands
Only execute a command if a condition is met.

ConditionalCommands is intended to be used when plugins have automatic commands that should only be executed if certain conditions are met.

## Usage
```
/cc unless <condition> do <command>
/cc if <condition> do <command>
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
<and>::='&' or '+'
<or>::='|'
<not>::='!'
<comparator>::='>'|'='|'<'
```
As shown above, only numbers can be compared, and placeholders can only consist of numbers. In case of multiple comparison operators in a group, i.e. 3>=<2, only the first operator will be used. Comparisons cannot includ spaces.

Examples:
```
/cc unless -ping->200 do kick konsolas
/cc if (-ping-<300&-ping->100)&-tps->15.0 do msg konsolas Your ping is between 300 and 100, and the TPS is greater than 15.
```

### Placeholders
Placeholders are delimited by '-'. Since they're applied with a replace, errors will probably be detected during parsing if they still exist.

 - ping: The latency of the tested player.
 - tps: Server TPS average over the last 2 seconds
 - time_online: Player's online time in milliseconds