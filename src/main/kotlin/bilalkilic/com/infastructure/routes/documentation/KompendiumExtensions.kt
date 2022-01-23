package bilalkilic.com.infastructure.routes.documentation

import io.bkbn.kompendium.Notarized.notarizedDelete
import io.bkbn.kompendium.Notarized.notarizedGet
import io.bkbn.kompendium.Notarized.notarizedPost
import io.bkbn.kompendium.Notarized.notarizedPut
import io.bkbn.kompendium.models.meta.MethodInfo
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

inline fun <reified TParam : Any, reified TResp : Any> Route.get(
    path: String,
    info: MethodInfo.GetInfo<TParam, TResp>,
    noinline body: PipelineInterceptor<Unit, ApplicationCall>,
): Route =
    route(path) {
        notarizedGet(info, body = body)
    }

inline fun <reified TParam : Any, reified TResp : Any> Route.get(
    info: MethodInfo.GetInfo<TParam, TResp>,
    noinline body: PipelineInterceptor<Unit, ApplicationCall>,
): Route = notarizedGet(info, body = body)

inline fun <reified TParam : Any, reified TReq : Any, reified TResp : Any> Route.post(
    path: String,
    info: MethodInfo.PostInfo<TParam, TReq, TResp>,
    noinline body: PipelineInterceptor<Unit, ApplicationCall>,
): Route =
    route(path) {
        notarizedPost(info, body = body)
    }

inline fun <reified TParam : Any, reified TReq : Any, reified TResp : Any> Route.put(
    path: String,
    info: MethodInfo.PutInfo<TParam, TReq, TResp>,
    noinline body: PipelineInterceptor<Unit, ApplicationCall>,
): Route =
    route(path) {
        notarizedPut(info, body = body)
    }

inline fun <reified TParam : Any, reified TResp : Any> Route.delete(
    path: String,
    info: MethodInfo.DeleteInfo<TParam, TResp>,
    noinline body: PipelineInterceptor<Unit, ApplicationCall>,
): Route =
    route(path) {
        notarizedDelete(info, body = body)
    }