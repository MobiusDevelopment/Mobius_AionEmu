#!/bin/sh

err=1
until [ $err == 0 ];
do

	java -Xms8m -Xmx32m -cp ../libs/*:AionEmu-Login.jar com.aionemu.loginserver.LoginServer
	err=$?
	lspid=$!
	echo ${lspid} > loginserver.pid
	echo "LoginServer started!"
	sleep 10
done
