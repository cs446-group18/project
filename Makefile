
.PHONY: run
run:
	./gradlew installDebug
	adb shell am start -n com.cs446group18.delaywise/com.cs446group18.delaywise.MainActivity

.PHONY: watch
watch:
	find app/src/main | entr $(MAKE) run

.PHONY: load
load:
	./gradlew publishImageToLocalRegistry

.PHONY: push
push: load
	docker push ghcr.io/cs446-group18/delaywise-server:latest

.PHONY: k8s_apply
k8s_apply: push
	kubectl apply -f k8s -n delaywise

.PHONY: clean
clean:
	./gradlew clean
	./gradlew uninstallAll

.PHONY: run_production
run_production:
	./gradlew installProductionDebug
	adb shell am start -n com.cs446group18.delaywise/com.cs446group18.delaywise.MainActivity
