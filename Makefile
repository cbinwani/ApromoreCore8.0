ICON_SIZE=24

all: png
	cp target/svg/project-diagram-solid.png bpmn-ui/src/main/resources/icon.png
	cp target/svg/folder-solid.png          folder-ui/src/main/resources/icon.png
	cp target/svg/file-solid.png            item-ui/src/main/resources/icon.png
	cp target/svg/tape-solid.png            metrics-ui/src/main/resources/icon.png
	cp target/svg/search-solid.png          pql-ui/src/main/resources/icon.png
	cp target/svg/puzzle-piece-solid.png    ui-spi/src/main/resources/icon.png
	cp target/svg/user-solid.png            user-ui/src/main/resources/icon.png

png:
	java -jar src/batik-1.11/batik-rasterizer-1.11.jar src/svg -d target/svg -cssUser src/icon.css -maxw $(ICON_SIZE) -maxh $(ICON_SIZE)
