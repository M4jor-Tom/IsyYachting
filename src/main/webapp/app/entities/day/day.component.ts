import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDay } from '@/shared/model/day.model';

import DayService from './day.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Day extends Vue {
  @Inject('dayService') private dayService: () => DayService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public days: IDay[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllDays();
  }

  public clear(): void {
    this.retrieveAllDays();
  }

  public retrieveAllDays(): void {
    this.isFetching = true;
    this.dayService()
      .retrieve()
      .then(
        res => {
          this.days = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IDay): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDay(): void {
    this.dayService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('isyYachtingApp.day.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDays();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
