import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import _root_.scenario.Generic
import conf.Config

class HesperidesApi extends Simulation {
    setUp(
        Generic.modules.inject(constantUsersPerSec(Config.defaultUserPerSeconds) during Config.duration).protocols(Config.httpConf)
    ).assertions(
        global.successfulRequests.percent.greaterThan(Config.percentOkMin),
        global.responseTime.mean.lessThan(Config.meanResponseTimeMax)
    )  
}
