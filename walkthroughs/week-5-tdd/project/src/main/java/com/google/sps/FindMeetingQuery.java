package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FindMeetingQuery {

  private static final int MINUTES_IN_DAY = 60 * 24;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Generate a list of meeting actions (start or end) of the required attendees sorted by time.
    // The order of 'start' and 'end' events with equal time permits 0 minute long meetings.
    List<MeetingAction> sortedAttendeeEvents = events.stream()
            .filter(event -> !Collections.disjoint(event.getAttendees(), request.getAttendees()))
            .map(Event::getWhen)
            .flatMap(event -> Stream.of(
                    new MeetingAction(event.start(), MeetingActionType.START),
                    new MeetingAction(event.end(), MeetingActionType.END)))
            .sorted(Comparator.comparing(MeetingAction::getTime).thenComparing(MeetingAction::getType))
            .collect(Collectors.toList());

    int curBusy = 0;
    int curFreeStart = 0;
    List<TimeRange> timeRanges = new ArrayList<>();
    for (MeetingAction event : sortedAttendeeEvents) {
      if (event.getType() == MeetingActionType.START) {
        TimeRange timeRange = new TimeRange(curFreeStart, event.getTime() - curFreeStart);
        if (curBusy == 0 && timeRange.duration() >= request.getDuration()) {
          timeRanges.add(timeRange);
        }
        curBusy++;
      } else {
        curBusy--;
        if (curBusy == 0) {
          curFreeStart = event.getTime();
        }
      }
    }
    TimeRange timeRangeUntilEndOfDay = new TimeRange(curFreeStart, MINUTES_IN_DAY - curFreeStart);
    if (timeRangeUntilEndOfDay.duration() >= request.getDuration()) {
      timeRanges.add(timeRangeUntilEndOfDay);
    }
    return timeRanges;
  }

  private static class MeetingAction {
    private final int time;
    private final MeetingActionType type;

    public MeetingAction(int time, MeetingActionType type) {
      this.time = time;
      this.type = type;
    }

    public int getTime() {
      return time;
    }

    public MeetingActionType getType() {
      return type;
    }
  }

  private enum MeetingActionType {
    END,
    START
  }
}
