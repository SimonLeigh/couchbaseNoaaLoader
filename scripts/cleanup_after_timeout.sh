cat cb_upload_log.txt | awk {'print substr($2, length($2)-26,length($2))'} > processednew.txt;
head -n -10 processednew.txt > processed.txt;
xargs -a processed.txt mv -t ./processed;