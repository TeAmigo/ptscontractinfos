Readme for ptscontractinfos

<2011-05-30 Mon 17:27>
Added the ability to take a file name as input. File consists of underlying symbol and related exchange,
for example:

    	GC, NYMEX
    	SI, NYMEX
    	ZS, ECBOT
    	ZW, ECBOT
    	ZC, ECBOT


<2011-05-24 Tue 17:20>
ptscontractinfos uses the Interactive Brokers tws-api to get futures contract information
and put it in the postgresql database. Currently uses a simple dialog and is one future at
a time.
