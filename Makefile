.PHONY: all build push

TAG:=dev-$(shell date +%s)

all: build push

build:
	@docker build --no-cache --pull -t wisvch/connect:${TAG}  .

push:
	@docker push wisvch/connect:${TAG}
