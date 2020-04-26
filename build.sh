#!/bin/bash
shopt -s extglob

cd $(dirname "$0")

function update-dep
	{
	if [ -d "$1" ]
	then
		cd "$1" && git pull && cd .. || exit 1
	else
		git clone "https://github.com/Unknow0/$1" || exit 1
	fi
	cd "$1" && mvn install && cd ../ || exit 1
	}

mkdir -p deps
cd deps
for dep in scene-builder sync unknow-common
do
	update-dep "$dep"
done
cd ..

git pull

mvn clean package assembly:assembly || exit 1

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
