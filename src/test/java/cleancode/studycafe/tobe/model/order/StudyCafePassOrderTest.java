package cleancode.studycafe.tobe.model.order;

import cleancode.studycafe.tobe.io.provider.LockerPassFileReader;
import cleancode.studycafe.tobe.io.provider.SeatPassFileReader;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPasses;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.pass.locker.StudyCafeLockerPasses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StudyCafePassOrderTest {

  @DisplayName("2주 이상 좌석권 결제시 10% 할인이 적용된다.")
  @Test
  void discount10PercentAppliedWhenSeatPassOver2Weeks() {
    // given
    SeatPassFileReader seatPassFileReader = new SeatPassFileReader();
    StudyCafeSeatPasses seatPasses = seatPassFileReader.getSeatPasses();
    StudyCafeSeatPass seatPass1 = seatPasses.findPassBy(StudyCafePassType.WEEKLY).stream()
      .filter(pass -> pass.getDuration() == 1)
      .findFirst()
      .get();
    StudyCafeSeatPass seatPass2 = seatPasses.findPassBy(StudyCafePassType.WEEKLY).stream()
      .filter(pass -> pass.getDuration() == 2)
      .findFirst()
      .get();

    // when
    StudyCafePassOrder passOrder1 = StudyCafePassOrder.of(seatPass1, null);
    StudyCafePassOrder passOrder2 = StudyCafePassOrder.of(seatPass2, null);

    // then
    double discountRate = 0.1;
    int discountPrice1 = (int) (seatPass1.getPrice() * (1 - discountRate));
    int discountPrice2 = (int) (seatPass2.getPrice() * (1 - discountRate));

    assertThat(passOrder1.getTotalPrice()).isNotEqualTo(discountPrice1);
    assertThat(passOrder1.getTotalPrice()).isEqualTo(seatPass1.getPrice());
    assertThat(passOrder2.getTotalPrice()).isEqualTo(discountPrice2);
  }

  @DisplayName("12주 좌석권 결제시 15% 할인이 적용된다.")
  @Test
  void discount15PercentAppliedWhenSeatPass12Weeks() {
    // given
    SeatPassFileReader seatPassFileReader = new SeatPassFileReader();
    StudyCafeSeatPasses seatPasses = seatPassFileReader.getSeatPasses();
    StudyCafeSeatPass seatPass = seatPasses.findPassBy(StudyCafePassType.WEEKLY).stream()
      .filter(pass -> pass.getDuration() == 12)
      .findFirst()
      .get();

    // when
    StudyCafePassOrder passOrder = StudyCafePassOrder.of(seatPass, null);

    // then
    double discountRate = 0.15;
    int discountPrice = (int) (seatPass.getPrice() * (1 - discountRate));

    assertThat(passOrder.getTotalPrice()).isEqualTo(discountPrice);
  }

  @DisplayName("이벤트 할인 금액은 좌석 이용권에만 적용되며, 사물함 이용권 금액과는 관련이 없다.")
  @Test
  void eventDiscountNotAppliedToLockerPass() {
    // given
    SeatPassFileReader seatPassFileReader = new SeatPassFileReader();
    StudyCafeSeatPasses seatPasses = seatPassFileReader.getSeatPasses();
    StudyCafeSeatPass seatPass = seatPasses.findPassBy(StudyCafePassType.FIXED).stream()
      .filter(pass -> pass.getDuration() == 12)
      .findFirst()
      .get();

    LockerPassFileReader lockerPassFileReader = new LockerPassFileReader();
    StudyCafeLockerPasses lockerPasses = lockerPassFileReader.getLockerPasses();
    StudyCafeLockerPass lockerPass = lockerPasses.findLockerPassBy(seatPass).get();

    // when
    StudyCafePassOrder passOrder = StudyCafePassOrder.of(seatPass, lockerPass);

    // then
    double discountRate = 0.15;

    assertThat(seatPass.getPrice()).isEqualTo(700000);
    assertThat(lockerPass.getPrice()).isEqualTo(30000);

    int discountedSeatPrice = (int) ((seatPass.getPrice()) * (1 - discountRate));

    assertThat(passOrder.getTotalPrice())
      .isEqualTo(discountedSeatPrice + lockerPass.getPrice());
    assertThat(passOrder.getDiscountPrice())
      .isEqualTo((int) (seatPass.getPrice() * discountRate));

    assertThat(passOrder.getTotalPrice())
      .isNotEqualTo(seatPass.getPrice() + lockerPass.getPrice());
  }
}
