package cleancode.studycafe.tobe.io.provider;

import cleancode.studycafe.tobe.model.pass.StudyCafePassType;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPass;
import cleancode.studycafe.tobe.model.pass.StudyCafeSeatPasses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SeatPassFileReaderTest {

  @DisplayName("좌석 이용권은 시간권/주간권/고정석 3종류가 존재한다.")
  @Test
  void seatPassHasHourlyWeeklyFixedTypes() {
    // given
    SeatPassFileReader seatPassFileReader = new SeatPassFileReader();
    StudyCafeSeatPasses seatPasses = seatPassFileReader.getSeatPasses();

    // when
    Map<StudyCafePassType, List<StudyCafeSeatPass>> passMap = Arrays.stream(StudyCafePassType.values())
      .collect(Collectors.toMap(type -> type, seatPasses::findPassBy));

    // then
    assertThat(passMap).hasSize(3);
    assertThat(passMap.keySet())
      .containsExactlyInAnyOrder(StudyCafePassType.HOURLY, StudyCafePassType.WEEKLY, StudyCafePassType.FIXED);
  }
}
