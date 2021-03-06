package com.beyondeye.reduks.logger

import com.beyondeye.reduks.logger.logformatter.LogFormatterSettings

/**
 *
 * Created by daely on 7/21/2016.
 */
class ReduksLoggerConfig<S>(
        /**
         *  activate log only if filter function returns true
         */
        @JvmField val filter:(prevState:S, action:Any)->Boolean={ state, action -> true},
        /**
         * Show diff between states.
         */
        @JvmField val logStateDiff:Boolean = false,
        /**
         * Filter function for showing states diff.
         */
        @JvmField val logStateDiffFilter:((nextState:S,action:Any)->Boolean)?=null,
        /**
         * Print the duration of each action?
         */
        @JvmField val logActionDuration:Boolean=false,
        /**
         * Should the logger catch, log, and re-throw errors?
         */
        @JvmField val logErrors:Boolean=true,
        /**
         * define log level as a function of log element and current log entry data
         * parameter logElement must be one of values defined in [LogElement]
         * return null if you don't to print log for the specificied input parameters combination
         */
        @JvmField val actionLevel:(logElement:Int,Action:Any,prevState:S,nextState:S?,error:Throwable?)->Int?=
                {le,a,ps,ns,e-> LogLevel.INFO },
        @JvmField val errorLevel:(logElement:Int,Action:Any,prevState:S,nextState:S?,error:Throwable?)->Int?=
            {le,a,ps,ns,e-> LogLevel.INFO },
        @JvmField val prevStateLevel:(logElement:Int,Action:Any,prevState:S,nextState:S?,error:Throwable?)->Int?=
         {le,a,ps,ns,e-> LogLevel.INFO },
        @JvmField val nextStateLevel:(logElement:Int,Action:Any,prevState:S,nextState:S?,error:Throwable?)->Int?=
        {le,a,ps,ns,e-> LogLevel.INFO },
        /**
         *  Returns `true` if current log entry should be collapsed
         */
        @JvmField val collapsed:(nextState:S?,action:Any)->Boolean={state,action -> false}, //
        /**
         * Transform state before processing it with reduks logger. By default no transformation
         */
        @JvmField val stateTransformer:(S)->S={s->s},
        /**
         * Transform action before processing it with reduks logger. By default no transformation
         */
        @JvmField val actionTransformer:(Any)->Any={a->a},
        /**
         * extract action type as string: by default return action class name
         */
        @JvmField val actionTypeExtractor:(Any)->String = {a->a.javaClass.simpleName},
        /**
         *  Transform error before print. By default no transformation
         */
        @JvmField val errorTransformer:(Throwable)->Throwable = {e->e},

        /**
         * main tag to use for logger output
         */
        @JvmField val reduksLoggerTag:String="REDUKS",
        /**
         * log formatter setting
         */
        @JvmField val formatterSettings:LogFormatterSettings= LogFormatterSettings()
)