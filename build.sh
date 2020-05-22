#!/bin/bash
shopt -s extglob

cd $(dirname "$0")

git pull

mvn clean package assembly:assembly \
       javadoc:aggregate -Dquiet=true -DjavadocExecutable=/usr/lib/jvm/java-1.8.0-openjdk-amd64/bin/javadoc -Dshow=private -Dlinks= \
       || exit 1


docker build -t kyhtanil/server -f Dockerfile.server target || exit 1
docker build -t kyhtanil/sync -f Dockerfile.sync target || exit 2

restart=
purge=
while [ "$1" ]
do
	case "$1" in
		purge-db)
			purge=1
			;;
		restart)
			restart=1
			;;
	esac
	shift
done

if [[ "$restart" ]]
then
	docker stop kyhtanil-server kyhtanil-sync
	docker rm  kyhtanil-server kyhtanil-sync

	[ "$purge" ] && ./purge-db.sh

	docker run -d --name kyhtanil-sync --restart=always -p 54320:54320 kyhtanil/sync
	docker run -d --name kyhtanil-server --restart=always -p 54321:54321 --add-host db:192.168.1.99 kyhtanil/server
fi
