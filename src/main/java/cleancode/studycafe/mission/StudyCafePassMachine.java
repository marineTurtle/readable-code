package cleancode.studycafe.mission;

import cleancode.studycafe.mission.exception.AppException;
import cleancode.studycafe.mission.io.InputHandler;
import cleancode.studycafe.mission.io.OutputHandler;
import cleancode.studycafe.mission.io.StudyCafeFileHandler;
import cleancode.studycafe.mission.model.StudyCafeLockerPass;
import cleancode.studycafe.mission.model.StudyCafePass;
import cleancode.studycafe.mission.model.StudyCafePassType;

import java.util.List;

public class StudyCafePassMachine {

  public static final StudyCafeFileHandler STUDY_CAFE_FILE_HANDLER = new StudyCafeFileHandler();
  private final InputHandler inputHandler = new InputHandler();
  private final OutputHandler outputHandler = new OutputHandler();

  public void run() {
    try {
      outputHandler.showWelcomeMessage();
      outputHandler.showAnnouncement();
      outputHandler.askPassTypeSelection();

      // 이용권 종류(시간/주/고정) 획득
      StudyCafePassType studyCafePassType = inputHandler.getPassTypeSelectingUserAction();

      // 이용권 목록에서 사용자가 선택한 이용권 종류를 필터링 한다.
      List<StudyCafePass> studyCafePasses = STUDY_CAFE_FILE_HANDLER.readStudyCafePasses();
      List<StudyCafePass> passes = studyCafePasses.stream()
        .filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
        .toList();
      outputHandler.showPassListForSelection(passes);

      // 최종 선택된 금액 포함 이용권
      StudyCafePass selectedPass = inputHandler.getSelectPass(passes);

      if (studyCafePassType == StudyCafePassType.FIXED) {
        // 고정석+이용기간과 일치하는 사물함 목록 출력
        List<StudyCafeLockerPass> lockerPasses = STUDY_CAFE_FILE_HANDLER.readLockerPasses();
        StudyCafeLockerPass lockerPass = lockerPasses.stream()
          .filter(option ->
            option.getPassType() == selectedPass.getPassType()
              && option.getDuration() == selectedPass.getDuration()
          )
          .findFirst()
          .orElse(null);
        outputHandler.askLockerPass(lockerPass);

        boolean lockerSelection = inputHandler.getLockerSelection();
        if (lockerSelection) {
          outputHandler.showPassOrderSummary(selectedPass, lockerPass);
          return;
        }
      }

      outputHandler.showPassOrderSummary(selectedPass, null);
    } catch (AppException e) {
      outputHandler.showSimpleMessage(e.getMessage());
    } catch (Exception e) {
      outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
    }
  }

}
