package net.common.common;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import net.admins.service.CommonService;
import net.admins.vo.WatchVO;
import net.sf.ehcache.CacheManager;

/**
 * @author 박정은
 * @since
 * @version
 * @see
 */
@RestController
public class WatchController {
    private WatchKey watchKey;
    private final static WatchController INSTANCE = new WatchController();
    
    public static WatchController getInstance() {
        return INSTANCE;
    }
    
    public void watchService(WatchVO watchVO) throws IOException {
        //watchService 생성
    	WatchService watchService = FileSystems.getDefault().newWatchService();
        //경로 생성
        Path path = Paths.get(watchVO.getFilePath());
        //해당 디렉토리 경로에 와치서비스와 이벤트 등록
        path.register(watchService,
            //StandardWatchEventKinds.ENTRY_CREATE,
            //StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
            //,StandardWatchEventKinds.OVERFLOW
            );
        
        Thread thread = new Thread(()-> {
            while(true) {
                try {
                    watchKey = watchService.take();//이벤트가 오길 대기(Blocking)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<WatchEvent<?>> events = watchKey.pollEvents();//이벤트들을 가져옴
                for(WatchEvent<?> event : events) {
                    //이벤트 종류
                    Kind<?> kind = event.kind();
                    System.out.println("경로출력" + watchVO.getFilePath() + watchVO.getFileName());
                    if(kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                    	Path changed = (Path) event.context();
                    	try {
                    		// cacheable을 이용하여 담아둔 파일일 경우에만... 해당 부분이 없으면 디렉토리의 어떤 파일이 변경되어도 cache가 clear 됨
                    		if(changed.endsWith(watchVO.getFileName())) {
                    			//CacheManager 사용해서 cache clear
                    			CacheManager.getInstance().getCache(watchVO.getCacheValue()).remove(watchVO.getCacheKey());
                    		}
                    		
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
                //sleep 1초에 한번씩
                if(!watchKey.reset()) {
                    try {
                        watchService.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
