#!/bin/sh

npm install iconv-lite buffer node-bitmap 

browserify -r iconv-lite -r buffer -r node-bitmap > ../../../main/resources/iconv-lite.js

rm -r ./node_modules