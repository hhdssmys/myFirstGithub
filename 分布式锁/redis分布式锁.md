#redis 分布式锁的实现  

本锁采用 redis.setnx() 实现分布式锁，具体代码如下：  

public class RedisDistributedLock {

    //单位ms
    private final static long KEY_EXPIRETIME = 50000;
    private final static long RE_GETLOCK_INTERVAL = 500;
    private final static long THREAD_GETLOCK_EXPIRETIME = 20000;//20000
    private final static Random random = new Random();
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisDistributedLock.class);
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 给指定key值加锁，加锁失败阻塞，成功方法结束
     * @param key
     */
    public void lock(String key) throws Exception{
         lock(key,KEY_EXPIRETIME,THREAD_GETLOCK_EXPIRETIME,RE_GETLOCK_INTERVAL);
    }

    /**
     * 给指定key值加锁，加锁失败阻塞，成功方法结束
     * @param key
     * @param keyExpireTime 关键字过期时间
     * @param threadGetLockExpireTime 线程尝试获取锁的最大时间
     * @param reGetLockInterval  获取锁的重试时间
     * @throws Exception
     */
    public void lock(String key,long keyExpireTime,long threadGetLockExpireTime,long reGetLockInterval) throws Exception{
        LOGGER.info("加锁：加锁的key为{}",key);
        ValueOperations<String,Long> valueOperations =  redisTemplate.opsForValue();
        boolean lockFlag = false;
        //随机因子:计划是三位数，因为单位是毫秒
        long randomFactor = 0;
        //超时因子，默认超时
        long oldExpireTime = 0;
        long currentExpireTime = 0;
        //获取锁超时
        long getLockTime = System.currentTimeMillis();
        long getLockTimeInterval = 0;
        while (true){
            //尝试加锁
            lockFlag =  valueOperations.setIfAbsent(key,(System.currentTimeMillis()+keyExpireTime));
            LOGGER.info("key{}正常加锁结果为{}",key,lockFlag);
            if(lockFlag){
                //加锁成功
                LOGGER.info("加锁：key为{}加锁空资源成功",key);
                break;
            }else{
                //加锁失败
                oldExpireTime = valueOperations.get(key);
                if(System.currentTimeMillis()>oldExpireTime){
                    //已超时，允许其他线程获取锁
                  currentExpireTime = valueOperations.getAndSet(key,System.currentTimeMillis()+keyExpireTime);
                    if(currentExpireTime == oldExpireTime){
                        //由于其他超时，自己获取倒了锁
                        LOGGER.info("加锁：key为{}加锁超时资源成功",key);
                        break;
                    }else{
                        //没有，重试
                        LOGGER.info("加锁：key为{}锁被截胡了，重试",key);
                    }
                }else{
                    //未超时，重试
                    LOGGER.info("锁未超时，重试");
                }
                //是否获取锁超时、
                getLockTimeInterval = System.currentTimeMillis()-getLockTime;
                if(getLockTimeInterval>threadGetLockExpireTime){
                    LOGGER.info("key{}长时间获取不到锁超时程序结束,获取用时{}ms",key,getLockTimeInterval);
                    throw new Exception("长时间获取不到锁超时程序结束");
                }
                //重试
                randomFactor = random.nextInt((int)reGetLockInterval)+100;
                LOGGER.info("加锁：key{}休眠{}ms后重试",key,randomFactor);
                try{
                    Thread.sleep(randomFactor);
                }catch(Exception e){
                    LOGGER.error(e.getMessage(),e);
                }
            }
        }
    }

    /**
     * 给指定key释放锁
     * @param key
     */
    public void unlock(String key){
        LOGGER.info("解锁：解锁的key为{}",key);
        long currentExpireTime = (Long)redisTemplate.opsForValue().get(key);
        //判断是否超时
        if(System.currentTimeMillis()<currentExpireTime){
            //未超时,正常释放
            redisTemplate.delete(key);
            LOGGER.info("解锁：key{}正常删除锁资源",key);
        }else{
            //已超时，其他线程已获取锁，不需要释放
            LOGGER.info("解锁：key{}锁资源超时",key);
        }
    }

}  
