# -COMMON-UTIL
유틸리티성 빈 및 static 클래스들을 보유한 모듈



## Initial settings
해당 모듈을 정상적으로 사용하려면 다음과 같은 설정을 해야 함
1.  메인 프로젝트의 `build.gradle` 파일에 모듈 추가  
	``` GRADLE  
	// build.gradle

	// ...
	dependencies {
		// ...
		// dependencies에 -common-util 프로젝트를 추가함으로써 해당 모듈을 사용 가능함
		implementation project(':common-util')
		// ...
	}
	// ...
	```


# - 엑셀 공통 작업 처리
