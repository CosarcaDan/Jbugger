import {Pipe,PipeTransform} from "@angular/core";

@Pipe({
  name:'summary'
})
export class SummaryPipe implements PipeTransform{
  transform(value: string, args?: any): any {
    if(!value){
      return null;
    }
    if(value.length > 15) {
      return value.substr(0, 15) + " .... ";
    }else{
      return value;
    }

  }

}
