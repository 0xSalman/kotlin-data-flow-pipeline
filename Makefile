# Makefile
#

help:
	@echo "Usage: {options} make [target ...]"
	@echo
	@echo "Commands:"
	@echo "  build    Build application"
	@echo "  run      Run application"
	@echo
	@echo "  help     Show available commands"
	@echo
	@echo "Examples:"
	@echo "  # Getting started"
	@echo "  make build run"
	@echo

build:
	@ gradle clean build
  
run:
	@ java -jar ./build/libs/assignment-1.0.jar

