export interface BackendError {
  errorCode:string,
  detailMessage:string,
  stackTrace:any[],
  suppressedExceptions:any[]
}
