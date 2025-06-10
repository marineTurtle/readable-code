package cleancode.studycafe.tobe.model.pass.locker;

import cleancode.studycafe.tobe.io.provider.LockerPassFileReader;
import cleancode.studycafe.tobe.io.provider.SeatPassFileReader;
import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPasses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StudyCafeLockerPassTest {

  @DisplayName("사물함 이용권의 기간은 고정석 이용권의 기간과 동일하다.")
  @Test
  void lockerAndSeatPassPeriodAreSame() {
    // given
    SeatPassFileReader seatPassFileReader = new SeatPassFileReader();
    StudyCafeSeatPasses seatPasses = seatPassFileReader.getSeatPasses();

    LockerPassFileReader lockerPassFileReader = new LockerPassFileReader();
    StudyCafeLockerPasses lockerPasses = lockerPassFileReader.getLockerPasses();

    // when
    List<StudyCafeSeatPass> fixedSeatPass = seatPasses.findPassBy(StudyCafePassType.FIXED);
    Optional<StudyCafeLockerPass> lockerPassOptional = lockerPasses.findLockerPassBy(fixedSeatPass.get(0));

    // then
    assertThat(lockerPassOptional.get().isSameDuration(fixedSeatPass.get(0).getDuration())).isTrue();
  }
}
