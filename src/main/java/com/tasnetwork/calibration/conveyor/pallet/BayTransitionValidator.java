package com.tasnetwork.calibration.conveyor.pallet;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.PalletBayState;
import com.tasnetwork.spring.orm.model.PalletManage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BayTransitionValidator {

    public static void validate(PalletManage palletManage) {
        if (palletManage == null || palletManage.getPalleteBayStateList() == null) {
            return;
        }

        List<PalletBayState> sortedStates = palletManage.getPalleteBayStateList().stream()
                .filter(state -> state.getPalletBayEntryTimeEpoch() != null && !state.getPalletBayEntryTimeEpoch().isEmpty())
                .sorted(Comparator.comparingLong(PalletBayState::getNormalizedEntryTimeMilli))
                .collect(Collectors.toList());

        StringBuilder errorDetails = new StringBuilder();

        for (int i = 0; i < sortedStates.size() - 1; i++) {
            PalletBayState currentState = sortedStates.get(i);
            PalletBayState nextState = sortedStates.get(i + 1);

            if (currentState.getPalletBayExitTimeEpoch() == null || currentState.getPalletBayExitTimeEpoch().isEmpty()) {
                errorDetails.append(String.format("Bay %s exited without timestamp. ", currentState.getBayStateKey()));
                continue;
            }

            try {
                long currentExit = currentState.getNormalizedExitTimeMilli();
                long nextEntry = nextState.getNormalizedEntryTimeMilli();

                if (nextEntry < currentExit) {
                    errorDetails.append(String.format("Overlap: Entered %s before exiting %s. ", 
                            nextState.getBayStateKey(), currentState.getBayStateKey()));
                }
            } catch (NumberFormatException e) {
                ApplicationLauncher.logger.error("BayTransitionValidator parsing error: " + e.getMessage());
            }
        }

        palletManage.setTransitionErrorDetails(errorDetails.toString().trim());
    }
}
