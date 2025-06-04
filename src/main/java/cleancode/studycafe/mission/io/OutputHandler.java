package cleancode.studycafe.mission.io;

import cleancode.studycafe.asis.exception.AppException;
import cleancode.studycafe.mission.model.*;

import java.util.List;
import java.util.Optional;

public class OutputHandler {

    public void showWelcomeMessage() {
        System.out.println("*** 프리미엄 스터디카페 ***");
    }

    public void showAnnouncement() {
        System.out.println("* 사물함은 고정석 선택 시 이용 가능합니다. (추가 결제)");
        System.out.println("* !오픈 이벤트! 2주권 이상 결제 시 10% 할인, 12주권 결제 시 15% 할인! (결제 시 적용)");
        System.out.println();
    }

    public void askPassTypeSelection() {
        System.out.println("사용하실 이용권을 선택해 주세요.");
        System.out.println("1. 시간 이용권(자유석) | 2. 주단위 이용권(자유석) | 3. 1인 고정석");
    }

    public void showPassListForSelection(List<StudyCafeSeatPass> passes) {
        System.out.println();
        System.out.println("이용권 목록");
        int orderNumber = 1;
        for (StudyCafeSeatPass pass : passes) {
            System.out.println(String.format("%s. ", orderNumber++) + display(pass));
        }
    }

    public void askLockerPass(StudyCafeLockerPass lockerPass) {
        System.out.println();
        System.out.println(String.format("사물함을 이용하시겠습니까? (%s)", display(lockerPass)));
        System.out.println("1. 예 | 2. 아니오");
    }

    public void showPassOrderSummary(StudyCafePasses studyCafePasses) {
        if (studyCafePasses == null) {
            throw new AppException("이용권 정보가 없습니다.");
        }

        System.out.println();
        System.out.println("이용 내역");

        Optional<StudyCafeSeatPass> seatPassOptional = studyCafePasses.getSeatPass();
        seatPassOptional.ifPresent(seatPass -> System.out.println("이용권: " + display(seatPass)));

        Optional<StudyCafeLockerPass> lockerPassOptional = studyCafePasses.getLockerPass();
        lockerPassOptional.ifPresent(lockerPass -> System.out.println("사물함: " + display(lockerPass)));

        if (studyCafePasses.hasDiscountPrice()) {
            System.out.println("이벤트 할인 금액: " + studyCafePasses.getDiscountPrice() + "원");
        }

        System.out.println("총 결제 금액: " + studyCafePasses.getTotalPrice() + "원");
        System.out.println();
    }

    public String display(StudyCafePass studyCafePass) {
        StudyCafePassType passType = studyCafePass.getPassType();
        int duration = studyCafePass.getDuration();
        int price = studyCafePass.getPrice();
        if (passType == StudyCafePassType.HOURLY) {
            return String.format("%s시간권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.WEEKLY) {
            return String.format("%s주권 - %d원", duration, price);
        }
        if (passType == StudyCafePassType.FIXED) {
            return String.format("%s주권 - %d원", duration, price);
        }
        return "";
    }
    
    public void showSimpleMessage(String message) {
        System.out.println(message);
    }
}
