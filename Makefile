	build_number = $(shell date +%Y%m%d%H%M%S)
	build_type = local

all:
	mvn jfx:jar -Dbuild=$(build_number) -Drelease=$(build_type)

native:
	mvn jfx:native -Dbuild=$(build_number) -Drelease=$(build_type)

jenkins:
	build_number = ${BUILD_NUMBER}
	build_type = ${JENKINS_BUILD_RELEASE}

	mvn install -Dbuild=$(build_number) -Drelease=$(build_type)


clean:
	find ./target -name *.jar -exec rm -f {} \;
