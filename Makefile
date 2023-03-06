
.PHONY: run
run:
	./gradlew installDebug
	adb shell am start -n com.cs446group18.delaywise/com.cs446group18.delaywise.MainActivity

.PHONY: watch
watch:
	find app/src/main | entr $(MAKE) run
