import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Circonstances } from './circonstances.model';
import { CirconstancesPopupService } from './circonstances-popup.service';
import { CirconstancesService } from './circonstances.service';

@Component({
    selector: 'jhi-circonstances-delete-dialog',
    templateUrl: './circonstances-delete-dialog.component.html'
})
export class CirconstancesDeleteDialogComponent {

    circonstances: Circonstances;

    constructor(
        private circonstancesService: CirconstancesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.circonstancesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'circonstancesListModification',
                content: 'Deleted an circonstances'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-circonstances-delete-popup',
    template: ''
})
export class CirconstancesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private circonstancesPopupService: CirconstancesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.circonstancesPopupService
                .open(CirconstancesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
