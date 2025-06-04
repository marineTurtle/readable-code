package cleancode.studycafe.mission.model;

import cleancode.studycafe.mission.exception.AppException;

import java.util.Arrays;

public enum StudyCafePassType {

    HOURLY(1, "시간 단위 이용권(자유석)"),
    WEEKLY(2, "주 단위 이용권(자유석)"),
    FIXED(3, "1인 고정석");

    private final Integer number;
    private final String description;

    StudyCafePassType(Integer number, String description) {
        this.number = number;
        this.description = description;
    }

    public static StudyCafePassType findBy(String inputNumber) {
        return Arrays.stream(values())
                .filter(type -> type.number.equals(Integer.parseInt(inputNumber)))
                .findFirst()
                .orElseThrow(() -> new AppException("잘못된 입력입니다."));
    }

    public static boolean isLockerAvailable(StudyCafePassType passType) {
        return StudyCafePassType.FIXED == passType;
    }
}
