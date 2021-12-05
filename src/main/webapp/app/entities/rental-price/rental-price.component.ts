import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IRentalPrice } from '@/shared/model/rental-price.model';

import RentalPriceService from './rental-price.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class RentalPrice extends Vue {
  @Inject('rentalPriceService') private rentalPriceService: () => RentalPriceService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public rentalPrices: IRentalPrice[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllRentalPrices();
  }

  public clear(): void {
    this.retrieveAllRentalPrices();
  }

  public retrieveAllRentalPrices(): void {
    this.isFetching = true;
    this.rentalPriceService()
      .retrieve()
      .then(
        res => {
          this.rentalPrices = res.data;
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

  public prepareRemove(instance: IRentalPrice): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeRentalPrice(): void {
    this.rentalPriceService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('isyYachtingApp.rentalPrice.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllRentalPrices();
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
