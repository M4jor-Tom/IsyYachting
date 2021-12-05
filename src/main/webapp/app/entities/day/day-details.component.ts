import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDay } from '@/shared/model/day.model';
import DayService from './day.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class DayDetails extends Vue {
  @Inject('dayService') private dayService: () => DayService;
  @Inject('alertService') private alertService: () => AlertService;

  public day: IDay = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dayId) {
        vm.retrieveDay(to.params.dayId);
      }
    });
  }

  public retrieveDay(dayId) {
    this.dayService()
      .find(dayId)
      .then(res => {
        this.day = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
