package org.apache.spark

import org.apache.log4j.{LogManager, PropertyConfigurator}
import org.slf4j.{Logger, LoggerFactory}
import org.slf4j.impl.StaticLoggerBinder

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.util.Utils

/*
Spark2.0及以上版本与Kafka兼容问题解决
 */
/**
 * :: DeveloperApi ::
 * Utility trait for classes that want to log data. Creates a SLF4J logger for the class and allows
 * logging messages at different levels using methods that only evaluate parameters lazily if the
 * log level is enabled.
 *
 * NOTE: DO NOT USE this class outside of Spark. It is intended as an internal utility.
 *       This will likely be changed or removed in future releases.
 */
@DeveloperApi
trait Logging {
	// Make the log field transient so that objects with Logging can
	// be serialized and used on another machine
	@transient private var log_ : Logger = null

	// Method to get the logger name for this object
	protected def logName = {
		// Ignore trailing $'s in the class names for Scala objects
		this.getClass.getName.stripSuffix("$")
	}

	// Method to get or create the logger for this object
	protected def log: Logger = {
		if (log_ == null) {
			initializeIfNecessary()
			log_ = LoggerFactory.getLogger(logName)
		}
		log_
	}

	// Log methods that take only a String
	protected def logInfo(msg: => String) {
		if (log.isInfoEnabled) log.info(msg)
	}

	protected def logDebug(msg: => String) {
		if (log.isDebugEnabled) log.debug(msg)
	}

	protected def logTrace(msg: => String) {
		if (log.isTraceEnabled) log.trace(msg)
	}

	protected def logWarning(msg: => String) {
		if (log.isWarnEnabled) log.warn(msg)
	}

	protected def logError(msg: => String) {
		if (log.isErrorEnabled) log.error(msg)
	}

	// Log methods that take Throwables (Exceptions/Errors) too
	protected def logInfo(msg: => String, throwable: Throwable) {
		if (log.isInfoEnabled) log.info(msg, throwable)
	}

	protected def logDebug(msg: => String, throwable: Throwable) {
		if (log.isDebugEnabled) log.debug(msg, throwable)
	}

	protected def logTrace(msg: => String, throwable: Throwable) {
		if (log.isTraceEnabled) log.trace(msg, throwable)
	}

	protected def logWarning(msg: => String, throwable: Throwable) {
		if (log.isWarnEnabled) log.warn(msg, throwable)
	}

	protected def logError(msg: => String, throwable: Throwable) {
		if (log.isErrorEnabled) log.error(msg, throwable)
	}

	protected def isTraceEnabled(): Boolean = {
		log.isTraceEnabled
	}

	private def initializeIfNecessary() {
		if (!Logging.initialized) {
			Logging.initLock.synchronized {
				if (!Logging.initialized) {
					initializeLogging()
				}
			}
		}
	}

	private def initializeLogging() {
		// Don't use a logger in here, as this is itself occurring during initialization of a logger
		// If Log4j 1.2 is being used, but is not initialized, load a default properties file
		val binderClass = StaticLoggerBinder.getSingleton.getLoggerFactoryClassStr
		// This distinguishes the log4j 1.2 binding, currently
		// org.slf4j.impl.Log4jLoggerFactory, from the log4j 2.0 binding, currently
		// org.apache.logging.slf4j.Log4jLoggerFactory
		val usingLog4j12 = "org.slf4j.impl.Log4jLoggerFactory".equals(binderClass)

		lazy val isInInterpreter: Boolean = {
			try {
				val interpClass = classForName("org.apache.spark.repl.Main")
				interpClass.getMethod("interp").invoke(null) != null
			} catch {
				case _: ClassNotFoundException => false
			}
		}
		def classForName(className: String): Class[_] = {
			Class.forName(className, true, getContextOrSparkClassLoader)
			// scalastyle:on classforname
		}
		def getContextOrSparkClassLoader: ClassLoader =
			Option(Thread.currentThread().getContextClassLoader).getOrElse(getSparkClassLoader)
		def getSparkClassLoader: ClassLoader = getClass.getClassLoader

		if (usingLog4j12) {
			val log4j12Initialized = LogManager.getRootLogger.getAllAppenders.hasMoreElements
			if (!log4j12Initialized) {
				// scalastyle:off println
				if (isInInterpreter) {
					val replDefaultLogProps = "org/apache/spark/log4j-defaults-repl.properties"
					Option(Utils.getSparkClassLoader.getResource(replDefaultLogProps)) match {
						case Some(url) =>
							PropertyConfigurator.configure(url)
							System.err.println(s"Using Spark's repl log4j profile: $replDefaultLogProps")
							System.err.println("To adjust logging level use sc.setLogLevel(\"INFO\")")
						case None =>
							System.err.println(s"Spark was unable to load $replDefaultLogProps")
					}
				} else {
					val defaultLogProps = "org/apache/spark/log4j-defaults.properties"
					Option(Utils.getSparkClassLoader.getResource(defaultLogProps)) match {
						case Some(url) =>
							PropertyConfigurator.configure(url)
							System.err.println(s"Using Spark's default log4j profile: $defaultLogProps")
						case None =>
							System.err.println(s"Spark was unable to load $defaultLogProps")
					}
				}
				// scalastyle:on println
			}
		}
		Logging.initialized = true

		// Force a call into slf4j to initialize it. Avoids this happening from multiple threads
		// and triggering this: http://mailman.qos.ch/pipermail/slf4j-dev/2010-April/002956.html
		log
	}
}

private object Logging {
	@volatile private var initialized = false
	val initLock = new Object()
	try {
		// We use reflection here to handle the case where users remove the
		// slf4j-to-jul bridge order to route their logs to JUL.
		val bridgeClass = Utils.classForName("org.slf4j.bridge.SLF4JBridgeHandler")
		bridgeClass.getMethod("removeHandlersForRootLogger").invoke(null)
		val installed = bridgeClass.getMethod("isInstalled").invoke(null).asInstanceOf[Boolean]
		if (!installed) {
			bridgeClass.getMethod("install").invoke(null)
		}
	} catch {
		case e: ClassNotFoundException => // can't log anything yet so just fail silently
	}
}

