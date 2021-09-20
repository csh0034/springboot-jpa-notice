package com.ask.sample.listener;

import com.ask.sample.config.SettingProperties;
import com.ask.sample.util.FileUtils;
import java.io.File;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadDirRemoveListener {

  private final SettingProperties settingProperties;

  @EventListener
  public void contextClosedEvent(ContextClosedEvent e) {
    log.info("call contextClosedEvent : '{}' remove", settingProperties.getUploadDir());
    FileUtils.removeDir(new File(settingProperties.getUploadDir()));
  }
}
